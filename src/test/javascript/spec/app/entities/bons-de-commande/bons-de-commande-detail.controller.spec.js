'use strict';

describe('Controller Tests', function() {

    describe('Bons_de_commande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBons_de_commande, MockFournisseur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBons_de_commande = jasmine.createSpy('MockBons_de_commande');
            MockFournisseur = jasmine.createSpy('MockFournisseur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bons_de_commande': MockBons_de_commande,
                'Fournisseur': MockFournisseur
            };
            createController = function() {
                $injector.get('$controller')("Bons_de_commandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:bons_de_commandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
