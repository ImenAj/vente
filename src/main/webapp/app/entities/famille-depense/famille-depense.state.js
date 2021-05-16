(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('famille-depense', {
            parent: 'entity',
            url: '/famille-depense',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.familleDepense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/famille-depense/famille-depenses.html',
                    controller: 'FamilleDepenseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('familleDepense');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('famille-depense-detail', {
            parent: 'famille-depense',
            url: '/famille-depense/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.familleDepense.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/famille-depense/famille-depense-detail.html',
                    controller: 'FamilleDepenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('familleDepense');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FamilleDepense', function($stateParams, FamilleDepense) {
                    return FamilleDepense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'famille-depense',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('famille-depense-detail.edit', {
            parent: 'famille-depense-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-depense/famille-depense-dialog.html',
                    controller: 'FamilleDepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FamilleDepense', function(FamilleDepense) {
                            return FamilleDepense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('famille-depense.new', {
            parent: 'famille-depense',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-depense/famille-depense-dialog.html',
                    controller: 'FamilleDepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designationFamille: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('famille-depense', null, { reload: 'famille-depense' });
                }, function() {
                    $state.go('famille-depense');
                });
            }]
        })
        .state('famille-depense.edit', {
            parent: 'famille-depense',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-depense/famille-depense-dialog.html',
                    controller: 'FamilleDepenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FamilleDepense', function(FamilleDepense) {
                            return FamilleDepense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('famille-depense', null, { reload: 'famille-depense' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('famille-depense.delete', {
            parent: 'famille-depense',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-depense/famille-depense-delete-dialog.html',
                    controller: 'FamilleDepenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FamilleDepense', function(FamilleDepense) {
                            return FamilleDepense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('famille-depense', null, { reload: 'famille-depense' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
